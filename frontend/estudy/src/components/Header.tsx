import { AspectRatio } from "@/components/base/aspect-ratio"
import { RiLightbulbFlashLine } from "@remixicon/react"
import { Button } from "@/components/base/button"
import React, { memo } from "react"
import { useTheme } from "next-themes"
import { useAppStore } from "@/store/AppStore"
import type { IUserData } from "@/interface"
import { RegisterSelectionDialog } from "@/components/dialogs/RegisterSelectionDialog"
import { LoginSelectionDialog } from "@/components/dialogs/LoginSelectionDialog"
import { UserMenu } from './UserMenu';

const Header: React.FC = () => {
    const { theme, setTheme } = useTheme()
    const userData: IUserData | null = useAppStore((state) => state.userData)

    const toggleTheme = () => {
        setTheme(theme === "light" ? "dark" : "light")
    }

    return (<header className="flex h-16 items-center justify-between rounded-lg bg-primary px-5">
        <div className="w-36">
            <AspectRatio ratio={3 / 1}>
                <img src="/logo.png" alt="E-Study Logo" className="h-full w-full object-contain" />
            </AspectRatio>
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