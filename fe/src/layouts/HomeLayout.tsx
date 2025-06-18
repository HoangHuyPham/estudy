import { Footer, Header } from "@components"
import { Outlet } from "react-router"

export const HomeLayout: React.FC = () => {
    return <>
        <Header />
        <Outlet />
        <Footer />
    </>
}