import { AspectRatio } from "@/component/base/aspect-ratio"
import { RiLightbulbFlashLine } from "@remixicon/react"
import { Button } from "@/component/base/button"
import React, { memo } from "react"
import { useTheme } from "next-themes"
import { useAppStore } from "@/store/AppStore"
import type { IUserData } from "@/interface"
import { RegisterSelectionDialog } from "@/component/dialogs/RegisterSelectionDialog"
import { LoginSelectionDialog } from "@/component/dialogs/LoginSelectionDialog"
import { UserMenu } from '@/component/UserMenu';

const Header: React.FC = () => {
    const { theme, setTheme } = useTheme()
    const userData: IUserData | null = useAppStore((state) => state.userData)

    const toggleTheme = () => {
        setTheme(theme === "light" ? "dark" : "light")
    }

    return (<header className="flex h-16 items-center justify-between rounded-lg bg-primary px-5">
        <div className="flex items-center">
            <div className="w-16">
                <AspectRatio ratio={1 / 1}>
                    <img src="/logo.png" alt="E-Study Logo" className="h-full w-full" />
                </AspectRatio>
            </div>

            <h1 className="ml-2 font-heading text-4xl text-white font-bold">EStudy</h1>
            { userData?.roles.includes("TUTOR") && <span className="ml-2 rounded-md bg-orange-400 px-1 text-xl font-semibold text-white">Tutor</span> }
        </div>

        <div className="flex items-center gap-2">
            <Button className="cursor-pointer hover:bg-muted/10 " aria-label="Dark theme" size="lg" variant="default" onClick={toggleTheme}>
                <RiLightbulbFlashLine className="text-foreground" />
            </Button>

            {
                userData ? <UserMenu userData={userData} /> :
                    <span>
                        <RegisterSelectionDialog />
                        <LoginSelectionDialog />
                    </span>
            }
        </div>
    </header>)
}

const MemoizedHeader = memo(Header)

export { MemoizedHeader as Header }