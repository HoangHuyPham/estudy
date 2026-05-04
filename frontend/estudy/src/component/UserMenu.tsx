import { Avatar, AvatarFallback, AvatarImage } from "@/component/base/avatar"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/component/base/dropdown-menu"
import { RiUser2Line, RiLogoutBoxLine } from "@remixicon/react"
import { Button } from "@/component/base/button"
import React from "react"
import type { IUserData } from "@/interface"
import { useAppStore } from "@/store/AppStore"
import { axiosClient } from "@/lib/utils"

export const UserMenu: React.FC<{ userData: IUserData }> = ({ userData }) => {

    const setUserData = useAppStore((state) => state.setUserData)

    const handleLogout = () => {
        axiosClient.post("/auth/logout", { accessToken: localStorage.getItem("accessToken") }, { withCredentials: true }).then(() => {
            localStorage.removeItem("accessToken");
            setUserData(null);
        }).catch(err => {
            console.error(err);
        })
    }

    return <>
        <Avatar size="lg">
            <AvatarImage referrerPolicy="no-referrer" src={userData?.avatar} />
            <AvatarFallback>
                <RiUser2Line />
            </AvatarFallback>
        </Avatar>
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="secondary">
                    {userData?.displayName}
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="bg-primary">
                <DropdownMenuItem>
                    <RiUser2Line />
                    Profile
                </DropdownMenuItem >
                <DropdownMenuItem className="text-orange-400">
                    <RiUser2Line />
                    Dành cho giảng viên
                </DropdownMenuItem>
                <DropdownMenuItem className="text-red-400"> 
                    <RiUser2Line />
                    Dành cho admin
                </DropdownMenuItem>
                <DropdownMenuItem
                    variant="destructive"
                    onSelect={handleLogout}>
                    <RiLogoutBoxLine />
                    <span>Log out</span>
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    </>
}