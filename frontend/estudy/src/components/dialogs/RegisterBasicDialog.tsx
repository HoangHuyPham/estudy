import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/base/dialog"
import { Button } from "@/components/base/button";
import { Field, FieldError, FieldGroup } from "@/components/base/field";
import { Input } from "@/components/base/input";
import { RiBallPenLine, RiKeyLine, RiLoader2Line, RiMailAddLine, RiUserLine } from "@remixicon/react";
import { zodResolver } from "@hookform/resolvers/zod"
import { Controller, useForm } from "react-hook-form"
import * as z from "zod"
import { useState } from "react";
import { axiosClient } from "@/lib/utils";
import type { APIResponse, PreRegisterBasicRequest } from "@/interface";
import { toast } from "sonner";
import { fakerVI } from "@faker-js/faker";
import { Turnstile } from "@/components/Turnstile";
import { Endpoints } from "@/constant";
import { OTPRegisterDialog } from "./OTPRegisterDialog";


const formSchema = z
  .object({
    displayName: z.string().nonempty("Tên hiển thị không được để trống"),
    username: z
      .string()
      .min(3, "Tên đăng nhập phải có ít nhất 3 ký tự")
      .max(255, "Tên đăng nhập không được vượt quá 255 ký tự"),
    password: z
      .string()
      .min(8, "Mật khẩu phải có ít nhất 8 ký tự")
      .max(255, "Mật khẩu không được vượt quá 255 ký tự"),
    confirmPassword: z
      .string()
      .min(8, "Xác nhận mật khẩu phải có ít nhất 8 ký tự")
      .max(255, "Xác nhận mật khẩu không được vượt quá 255 ký tự"),
    email: z.email("Email không hợp lệ"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Mật khẩu xác nhận không khớp",
    path: ["confirmPassword"],
  })

export type RegisterBasicDialogProps = {
  toggleSelectionDialog: (isOpen: boolean) => void;
}

export const RegisterBasicDialog: React.FC<RegisterBasicDialogProps> = ({ toggleSelectionDialog }) => {
  const [open, setOpen] = useState(false);
  const [recaptchaToken, setRecaptchaToken] = useState<string | null>(null);
  const [otpId, setOtpId] = useState<string | null>(null);
  const [reCaptchaActive, setReCaptchaActive] = useState(false);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      displayName: fakerVI.person.fullName(),
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
    },
    mode: "onChange",
  })

  const onSubmit = async (input: z.infer<typeof formSchema>) => {
    setReCaptchaActive(true);

    if (!recaptchaToken) {
      toast.info("Vui lòng hoàn thành ReCAPTCHA", { richColors: true });
      return;
    }

    await axiosClient.post<APIResponse<string>>(Endpoints.auth.preRegister, { ...input, reCaptchaToken: recaptchaToken } as PreRegisterBasicRequest).then(res => {
      const { code, message, data } = res.data;
      if (code !== 200) {
        toast.error("Đăng ký thất bại", { description: message || "", richColors: true });
      } else {
        setOtpId(data);
        toast.success("Đăng ký thành công", { description: message, richColors: true });
      }
    }).catch(err => {
      toast.error("Đã có lỗi xảy ra khi đăng ký", { description: err.response?.data?.message || err.message, richColors: true });
      console.error(err);
    }).finally(() => {
      setRecaptchaToken(null);
      setReCaptchaActive(false);
    })
  }

  const onReCaptchaChange = (token: string | null) => {
    setRecaptchaToken(token);
    console.warn("ReCAPTCHA token:", token);
  }

  const handleOpenChange = (open: boolean) => { 
    if (!open) {
      setOtpId(null);
      setRecaptchaToken(null);
      setReCaptchaActive(false);
      form.reset();
    }
    setOpen(open);
  }

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        <Button variant="outline" size="lg">
          <RiUserLine /> Tên đăng nhập / Email
        </Button>
      </DialogTrigger>

      <DialogContent>
        {
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <DialogHeader>
              <DialogTitle>Đăng ký</DialogTitle>
            </DialogHeader>

            <FieldGroup className="py-4">

              <Controller
                disabled={!!otpId}
                name="displayName"
                control={form.control}
                render={({ field, fieldState }) => (
                  <>
                    <Field orientation="horizontal">
                      <RiBallPenLine aria-hidden="true" />
                      <Input
                        {...field}
                        placeholder="Tên hiển thị"
                        required
                      />
                    </Field>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </>
                )}
              />

              <Controller
                disabled={!!otpId}
                name="username"
                control={form.control}
                render={({ field, fieldState }) => (
                  <>
                    <Field orientation="horizontal">
                      <RiUserLine aria-hidden="true" />
                      <Input
                        {...field}
                        placeholder="Tên đăng nhập / Email"
                        required
                      />
                    </Field>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </>
                )} />

              <Controller
                disabled={!!otpId}
                name="email"
                control={form.control}
                render={({ field, fieldState }) => (
                  <>
                    <Field orientation="horizontal">
                      <RiMailAddLine aria-hidden="true" />
                      <Input
                        {...field}
                        type="email"
                        placeholder="Email"
                        aria-invalid={fieldState.invalid}
                        required
                      />
                    </Field>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </>
                )}
              />

              <Controller
                disabled={!!otpId}
                name="password"
                control={form.control}
                render={({ field, fieldState }) => (
                  <>
                    <Field orientation="horizontal">
                      <RiKeyLine aria-hidden="true" />
                      <Input
                        {...field}
                        type="password"
                        placeholder="Mật khẩu"
                        aria-invalid={fieldState.invalid}
                        required
                      />
                    </Field>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </>
                )}
              />

              <Controller
                disabled={!!otpId}
                name="confirmPassword"
                control={form.control}
                render={({ field, fieldState }) => (
                  <>
                    <Field orientation="horizontal">
                      <RiKeyLine aria-hidden="true" />
                      <Input
                        {...field}
                        type="password"
                        placeholder="Xác nhận mật khẩu"
                        aria-invalid={fieldState.invalid}
                        required
                      />
                    </Field>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </>
                )}
              />
              <Turnstile
                className="mx-auto"
                sitekey={import.meta.env.ESTUDY_CLOUDFLARE_TURNSTILE_SITE_KEY}
                onVerify={onReCaptchaChange}
                isActive={reCaptchaActive}
              />
              <Button type="submit" className="w-full" disabled={form.formState.isSubmitting || !!otpId}>
                {form.formState.isSubmitting && <RiLoader2Line className="animate-spin" />}
                Đăng ký
              </Button>
            </FieldGroup>
          </form>
        }
        {otpId && <OTPRegisterDialog otpId={otpId} email={form.getValues("email")} toggleSelectionDialog={toggleSelectionDialog} />}
      </DialogContent>
    </Dialog>
  )
}