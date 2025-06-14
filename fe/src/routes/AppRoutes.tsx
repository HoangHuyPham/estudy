import { Route, Routes } from "react-router"
import { Home } from "../pages/Home.tsx"
import { Login } from "../pages/Login.tsx"
import { Register } from "../pages/Register.tsx"


export const AppRoutes: React.FC = () => {
    return <>
        <Routes>
            {/* Public Route  */}
            <Route>
                <Route index element={<Home/>} />
                <Route path="/login" element={<Login onClose={function(): void {
                    throw new Error("Function not implemented.")
                } } />} />
                <Route path="/register" element={<Register onClose={function(): void {
                    throw new Error("Function not implemented.")
                } } />} />
            </Route>


            {/* Private Route  */}
            <Route path="/admin">
            </Route>

            <Route path="*" element={<p className="text-center font-bold text-4xl">This page is not available :(</p>}/>
        </Routes>
    </>
}