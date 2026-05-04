import { Tabs } from "@/component/base/tabs"
import { cn } from "@/lib/utils";
import { NavLink, Outlet } from "react-router-dom";


export const ManageCourseLayout: React.FC = () => {
    const navItems = [
        { label: "Khóa học", to: "courses" },
        { label: "Drafts", to: "drafts" },
    ];

    return (
        <>
            <h1 className="text-2xl font-bold px-2 pt-5">Quản lý khóa học</h1>
            <Tabs className="flex w-full flex-col gap-4 py-4" defaultValue={"courses"} value="courses">
                <div className="flex border-b border-muted">
                    {navItems.map((item) => (
                        <NavLink
                            key={item.to}
                            to={item.to}
                            className={({ isActive }) =>
                                cn(
                                    "px-4 py-2 text-sm font-medium transition-all relative",
                                    "hover:text-primary text-muted-foreground",
                                    isActive && "text-blue-500 after:absolute after:bottom-0 after:left-0 after:right-0 after:h-0.5 after:bg-blue-500"
                                )
                            }
                        >
                            {item.label}
                        </NavLink>
                    ))}
                </div>
            </Tabs>
            <div className="flex-col grow justify-center rounded-lg border-2 border-dashed border-muted">
                <Outlet />
            </div>
        </>
    )
}