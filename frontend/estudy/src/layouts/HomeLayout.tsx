import { Outlet } from "react-router-dom"
import { Sidebar, type NavItem } from "@/component/SideBar"
import { Header } from "@/component/Header"
import { useAppStore } from "@/store/AppStore"

const defaultNavItems: NavItem[] = [
    { label: "Trang chủ", to: "/home", icon: "ri-dashboard-line", activeIcon: "ri-dashboard-fill" },
    { label: "Khóa học miễn phí", to: "/free-courses", icon: "ri-book-open-line", activeIcon: "ri-book-open-fill" },
    { label: "Về chúng tôi", to: "/about", icon: "ri-group-line", activeIcon: "ri-group-fill" },
]

const tutorNavItems: NavItem[] = [
    ...defaultNavItems,
    { label: "Quản lý khóa học", to: "/tutor/manage-courses", icon: "ri-stack-line", activeIcon: "ri-stack-fill" },
]

export const HomeLayout: React.FC = () => {
    const userData = useAppStore((state) => state.userData)
    const navItems = userData?.roles?.includes("TUTOR") ? tutorNavItems : defaultNavItems


    return (
        <>
            <div className="min-h-screen bg-background text-foreground">
                <div className="mx-auto flex flex-col min-h-screen w-full max-w-screen-2xl  px-4 py-4 md:px-6 lg:px-8">
                    <Header />
                    <div className="flex grow">
                        <aside className="w-28 shrink-0 rounded-lg px-2">
                            <Sidebar navItems={navItems} />
                        </aside>

                        <main className="flex flex-col min-w-0 grow rounded-lg bg-card px-4">
                            <Outlet />
                        </main>
                    </div>
                </div>
            </div>
        </>
    )
}