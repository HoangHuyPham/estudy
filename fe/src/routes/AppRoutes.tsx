import { Route, Routes } from "react-router"
import { AdminLayout } from '../pages/admin/AdminLayout';
import { CourseManagement } from '../pages/admin/CourseManagement';
import { UserManagement } from '../pages/admin/UserManagement';
import { CartManagement } from '../pages/admin/CartManagement';
import { RoleManagement } from '../pages/admin/RoleManagement';
import { Login, Register } from "../pages"

export const AppRoutes: React.FC = () => {
    return <>
        <Routes>
            {/* Public Route  */}
            <Route path="/auth">
                <Route path="login" element={<Login/>} />
                <Route path="register" element={<Register/>} />
            </Route>

            {/* Private Route  */}
            <Route path="/admin" element={<AdminLayout />}>
                <Route path="course" element={<CourseManagement />} />
                <Route path="users" element={<UserManagement />} />
                <Route path="carts" element={<CartManagement />} />
                <Route path="roles" element={<RoleManagement />} />
            </Route>

            <Route path="*" element={<p className="text-center font-bold text-4xl">This page is not available :(</p>}/>
        </Routes>
    </>
}