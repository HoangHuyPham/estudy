import { NavLink } from "react-router-dom";
import { cn } from "@/lib/utils";
import type React from "react";
import { memo } from "react";

const navItems = [
    { label: "Trang chủ", to: "/home", icon: "ri-dashboard-line", activeIcon: "ri-dashboard-fill" },
    { label: "Khóa học của tôi", to: "/courses", icon: "ri-book-open-line", activeIcon: "ri-book-open-fill" },
    { label: "Học viên", to: "/students", icon: "ri-group-line", activeIcon: "ri-group-fill" },
];

const Sidebar :React.FC = () => {
    return (
        <nav className="flex w-full flex-col gap-4 px-2">
            {navItems.map((item) => (
                <NavLink
                    key={item.to}
                    to={item.to}
                    className={({ isActive }) =>
                        cn(
                            "group flex flex-col items-center justify-center gap-1.5 rounded-xl py-3 transition-all",
                            "hover:bg-accent hover:text-accent-foreground",
                            isActive
                                ? "bg-accent text-primary shadow-sm"
                                : "text-muted-foreground"
                        )
                    }
                >
                    {({ isActive }) => (
                        <>
                            <i className={cn(
                                "text-2xl transition-transform duration-200",
                                isActive ? `${item.activeIcon} scale-110` : item.icon,
                                "group-hover:scale-110"
                            )} />

                            <span className={cn(
                                "text-center text-[10px] font-medium uppercase tracking-wider transition-all",
                                isActive && "font-bold scale-105"
                            )}>
                                {item.label}
                            </span>
                        </>
                    )}
                </NavLink>
            ))}
        </nav>
    );
}

const MemoizedSidebar = memo(Sidebar)

export { MemoizedSidebar as Sidebar }