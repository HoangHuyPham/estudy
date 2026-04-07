import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/base/dialog"
import { Button } from "@/components/base/button";
import { LoginBasicDialog } from "@/components/dialogs/LoginBasicDialog";
import { useState } from "react";
import { GoogleLogin, type CredentialResponse } from "@react-oauth/google";
import { useTheme } from "next-themes";
import { toast } from "sonner";
import { axiosClient, parseAT } from "@/lib/utils";
import type { APIResponse, LoginWithGoogleRequest } from "@/interface";
import { useAppStore } from "@/store/AppStore";
import { Endpoints } from "@/constant";

export const LoginSelectionDialog: React.FC = () => {

  const setUserData = useAppStore((state) => state.setUserData)
  const { theme } = useTheme();
  const [open, setOpen] = useState(false);

  const handleLoginGoogle = (token: CredentialResponse | null) => {
    axiosClient.post<APIResponse<string>>(Endpoints.auth.loginWithGoogle, { token: token?.credential } as LoginWithGoogleRequest).then(res => {
      const { code, message, data } = res.data;
      if (code !== 200) {
        toast.error("Đăng nhập thất bại", { description: message || "", richColors: true });
      } else {
        toast.success("Đăng nhập thành công", { description: message, richColors: true });
        localStorage.setItem("accessToken", data);
        setUserData(parseAT(data));
        setOpen(false);
      }
    }).catch(err => {
      toast.error("Đã có lỗi xảy ra khi đăng nhập", { description: err.response?.data?.message || err.message, richColors: true });
      console.error(err);
    })
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button size="lg" className="bg-background text-foreground hover:bg-background/90">
          Đăng nhập
        </Button>
      </DialogTrigger>
      <DialogContent className="bg-background">
        <DialogHeader>
          <DialogTitle>Đăng nhập</DialogTitle>
          <LoginBasicDialog toggleSelectionDialog={setOpen} />
          <GoogleLogin
            logo_alignment="center"
            theme={theme === "dark" ? "filled_black" : "outline"}
            text="signin_with"
            onSuccess={handleLoginGoogle}
            onError={() => handleLoginGoogle(null)}
          />
        </DialogHeader>
      </DialogContent>
    </Dialog>
  )
}