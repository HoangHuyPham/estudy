import { Navigate, Route, Routes } from "react-router"
import { Home, CourseDetail, Cart } from "@pages"
import { HomeLayout, } from "@layouts"
import { CartProvider } from "@contexts/CartContext"
import { ProtectedRoute } from "./ProtectedRoute"


export const AppRoutes: React.FC = () => {
    return <>
        <Routes>
            {/* Public Route  */}
            <Route>
                <Route index element={<Navigate to={"/home"} />} />

                <Route path="/home" element={
                    <CartProvider>
                        <HomeLayout />
                    </CartProvider>
                }>
                    <Route index element={<Home />} />
                    <Route path="course/:productId" element={<CourseDetail />} />

                    <Route path="cart" element={<ProtectedRoute><Cart /></ProtectedRoute>} />
                </Route>
            </Route>

            {/* Private Route  */}
            <Route path="/admin">
            </Route>

            <Route path="*" element={<p className="text-center font-bold text-4xl">This page is not available :(</p>} />
        </Routes>
    </>
}