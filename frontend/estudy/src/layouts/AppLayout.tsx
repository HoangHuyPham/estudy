import { Outlet } from "react-router-dom"
import { Sidebar } from "@/components/SideBar"
import { cn } from "@/lib/utils"
import { Header } from "@/components/Header"

export const AppLayout: React.FC = () => {
    return (
        <>
            <div className={cn("min-h-screen bg-background text-foreground")}>
                <div className="mx-auto flex min-h-screen w-full max-w-screen-2xl flex-col px-4 py-4 md:px-6 lg:px-8">
                    <Header />
                    <div className="flex flex-1">
                        <aside className="w-28 shrink-0 rounded-lg px-2">
                            <Sidebar />
                        </aside>

                        <main className="min-w-0 flex-1 rounded-lg bg-card px-4">
                            <Outlet />
                        </main>
                    </div>
                </div>
            </div>
        </>
    )
}