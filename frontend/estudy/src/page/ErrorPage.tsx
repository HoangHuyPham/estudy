import { RiArrowLeftSLine, RiLoader2Line } from "@remixicon/react"
import { NavLink } from "react-router-dom"
import { Button } from "@/component/base/button"

export const ErrorPage: React.FC = () => {
    return (
        <div className="min-h-screen bg-background text-foreground">
            <div className="mx-auto flex min-h-screen w-full max-w-screen-2xl items-center justify-center px-4 py-4 md:px-6 lg:px-8">
                <section className="relative border-primary w-full max-w-xl overflow-hidden rounded-2xl border bg-card p-8 shadow-sm">
                    <div className="relative flex flex-col items-center text-center">
                        <span className="mb-4 inline-flex size-14 items-center justify-center rounded-full border border-border bg-background">
                            <RiLoader2Line className="size-8 animate-spin text-primary/70" />
                        </span>

                        <p className="text-sm text-muted-foreground">Lỗi điều hướng</p>
                        <h1 className="mt-2 text-3xl font-bold tracking-tight">404 - Không tìm thấy trang</h1>
                        <p className="mt-3 max-w-md text-sm text-muted-foreground">
                            Trang bạn đang tìm kiếm không tồn tại hoặc đã được di chuyển.
                        </p>

                        <Button asChild size="lg" className="mt-7 h-9 px-4 text-sm">
                            <NavLink to="/home">
                                <RiArrowLeftSLine className="size-4" />
                                Quay lại trang chủ
                            </NavLink>
                        </Button>
                    </div>
                </section>
            </div>
        </div>
    )
}