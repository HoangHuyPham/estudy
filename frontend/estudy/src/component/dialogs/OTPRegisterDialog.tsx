import {
    InputOTP,
    InputOTPGroup,
    InputOTPSlot,
} from "@/component/base/input-otp"
import { useState } from "react";
import { RiLoader2Line } from "@remixicon/react";
import { Button } from "@/component/base/button";
import { axiosClient, parseAT } from "@/lib/utils";
import { Endpoints } from "@/constant";
import type { RegisterOTPConfirmRequest } from "@/interface";
import { toast } from "sonner";
import { useAppStore } from "@/store/AppStore";


export const OTPRegisterDialog: React.FC<{ otpId: string, email: string, toggleSelectionDialog: (open: boolean) => void }> = ({ otpId, email, toggleSelectionDialog }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [otp, setOtp] = useState("");
    const setUserData = useAppStore((state) => state.setUserData)

    const handleConfirm = async () => {
        setIsLoading(true);

        await axiosClient.post(Endpoints.AUTH.REGISTER, {
            email: email,
            mailOTPId: otpId,
            otp: otp
        } as RegisterOTPConfirmRequest).then(res => {
            const { code, message, data } = res.data;
            if (code !== 200) {
                toast.error("Xác nhận OTP thất bại", { description: message || "", richColors: true });
            } else {
                toast.success("Xác nhận OTP thành công", { description: message, richColors: true });
                localStorage.setItem("accessToken", data);
                setUserData(parseAT(data));
                toggleSelectionDialog(false);
            }
        }).catch(err => {
            toast.error("Đã có lỗi xảy ra khi xác nhận OTP", { description: err.response?.data?.message || err.message, richColors: true });
            console.error(err);
        }).finally(() => {
            setIsLoading(false);
        })

    }

    return (<>
        <div className="text-center text-sm">
            Nhập OTP được gửi về email <strong>{email}</strong> của bạn.
        </div>
        <InputOTP
            maxLength={6}
            value={otp}
            onChange={(value) => setOtp(value)}
        >
            <InputOTPGroup className="mx-auto">
                <InputOTPSlot index={0} />
                <InputOTPSlot index={1} />
                <InputOTPSlot index={2} />
                <InputOTPSlot index={3} />
                <InputOTPSlot index={4} />
                <InputOTPSlot index={5} />
            </InputOTPGroup>
        </InputOTP>
        <Button className="w-full" disabled={isLoading} onClick={handleConfirm}>
            {isLoading && <RiLoader2Line className="animate-spin" />}
            Xác nhận
        </Button>
    </>)
}