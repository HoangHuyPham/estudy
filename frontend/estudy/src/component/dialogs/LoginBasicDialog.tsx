import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/component/base/dialog"
import { Button } from "@/component/base/button";
import { Field, FieldError, FieldGroup } from "@/component/base/field";
import { Input } from "@/component/base/input";
import { RiKeyLine, RiLoader2Line, RiUserLine } from "@remixicon/react";
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import * as z from "zod"
import { useState } from "react";
import { axiosClient, parseAT } from "@/lib/utils";
import type { APIResponse, LoginBasicRequest } from "@/interface";
import { toast } from "sonner";
import { useAppStore } from "@/store/AppStore";
import { Endpoints } from "@/constant";

const formSchema = z.object({
  username: z.string().nonempty("Tên đăng nhập không được để trống"),
  password: z.string().nonempty("Mật khẩu không được để trống"),
})

export type LoginBasicDialogProps = {
  toggleSelectionDialog: (isOpen: boolean) => void;
}

export const LoginBasicDialog: React.FC<LoginBasicDialogProps> = ({ toggleSelectionDialog }) => {
  const [open, setOpen] = useState(false);
  const setUserData = useAppStore((state) => state.setUserData)

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: "",
    },
    mode: "onChange",
  })

  const onSubmit = async (input: z.infer<typeof formSchema>) => {
    axiosClient.post<APIResponse<string>>(Endpoints.AUTH.LOGIN, input as LoginBasicRequest, {withCredentials: true}).then(res => {
      const { code, message, data } = res.data;
      if (code !== 200) {
        toast.error("Đăng nhập thất bại", { description: message || "", richColors: true });
      } else {
        toast.success("Đăng nhập thành công", { description: message, richColors: true });
        localStorage.setItem("accessToken", data);
        setUserData(parseAT(data));
        toggleSelectionDialog(false);
      }
    }).catch(err => {
      toast.error("Đã có lỗi xảy ra khi đăng nhập", { description: err.response?.data?.message || err.message, richColors: true });
      console.error(err);
    })
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" size="lg">
          <RiUserLine /> Tên đăng nhập / Email
        </Button>
      </DialogTrigger>

      <DialogContent>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <DialogHeader>
            <DialogTitle>Đăng nhập</DialogTitle>
          </DialogHeader>

          <FieldGroup className="py-4">

            <>
              <Field orientation="horizontal">
                <RiUserLine aria-hidden="true" />
                <Input
                  {...form.register("username")}
                  placeholder="Tên đăng nhập / Email"
                  aria-invalid={!!form.formState.errors.username}
                  required
                />
              </Field>
              {form.formState.errors.username && <FieldError errors={[form.formState.errors.username]} />}
            </>

            <>
              <Field orientation="horizontal">
                <RiKeyLine aria-hidden="true" />
                <Input
                  {...form.register("password")}
                  type="password"
                  placeholder="Mật khẩu"
                  aria-invalid={!!form.formState.errors.password}
                  required
                />
              </Field>
              {form.formState.errors.password && <FieldError errors={[form.formState.errors.password]} />}
            </>

            <a className="cursor-pointer text-sm text-blue-700 hover:underline">
              Quên mật khẩu?
            </a>
            <Button type="submit" className="w-full" disabled={form.formState.isSubmitting}>
              {form.formState.isSubmitting && <RiLoader2Line className="animate-spin" />}
              Đăng nhập
            </Button>
          </FieldGroup>
        </form>

      </DialogContent>
    </Dialog>
  )
}