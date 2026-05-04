import { useState, useEffect, useRef } from "react"
import { cn } from "@/lib/utils"

export const Floatter = ({ children, className }: { children: React.ReactNode; className?: string }) => {
    const [shouldSticky, setShouldSticky] = useState(false)
    const sentinelRef = useRef<HTMLDivElement>(null)

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                setShouldSticky(!entry.isIntersecting)
            }, {
            threshold: 1
        })

        if (sentinelRef.current) {
            observer.observe(sentinelRef.current)
        }

        return () => observer.disconnect()
    }, [])

    return (<div ref={sentinelRef} className={cn("w-full px-5", className)}>
        <div className={cn("w-full transition-all", shouldSticky ? "fixed top-0 left-0 border-2 rounded-2xl bg-transparent z-10 w-[70%] p-5 mx-[15%]" : "relative")}>
            {children}
        </div>
    </div>)
}