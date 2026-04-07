import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/base/dialog"
import { Button } from "@/components/base/button";
import { GoogleLogin, type CredentialResponse } from "@react-oauth/google";
import { useTheme } from "next-themes";
import { RegisterBasicDialog } from "./RegisterBasicDialog";
import { useState } from "react";
import { axiosClient, parseAT } from "@/lib/utils";
import { useAppStore } from "@/store/AppStore";
import { toast } from "sonner";
import type { APIResponse, LoginWithGoogleRequest } from "@/interface";
import { Endpoints } from "@/constant";

export const RegisterSelectionDialog: React.FC = () => {
  const { theme } = useTheme();
  const [open, setOpen] = useState(false);
  const setUserData = useAppStore((state) => state.setUserData)

  const handleLoginGoogle = (token: CredentialResponse | null) => {
    axiosClient.post<APIResponse<string>>(Endpoints.auth.loginWithGoogle, { token: token?.credential } as LoginWithGoogleRequest).then(res => {
      const { code, message, data } = res.data;
      if (code !== 200) {
        toast.error("Đăng ký thất bại", { description: message || "", richColors: true });
      } else {
        toast.success("Đăng ký thành công", { description: message, richColors: true });
        localStorage.setItem("accessToken", data);
        setUserData(parseAT(data));
        setOpen(false);
      }
    }).catch(err => {
      toast.error("Đã có lỗi xảy ra khi đăng ký", { description: err.response?.data?.message || err.message, richColors: true });
      console.error(err);
    })
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button size="lg" className="hover:bg-background/10">
          Đăng ký
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Đăng ký</DialogTitle>
          <RegisterBasicDialog toggleSelectionDialog={setOpen} />
          <GoogleLogin
            logo_alignment="center"
            theme={theme === "dark" ? "filled_black" : "outline"}
            text="signup_with"
            onSuccess={handleLoginGoogle}
            onError={() => handleLoginGoogle(null)}
          />
        </DialogHeader>
      </DialogContent>
    </Dialog >
  )
}