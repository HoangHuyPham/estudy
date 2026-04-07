import { Navigate, Route, Routes } from 'react-router-dom'
import { AuthRoute } from '@/routes/AuthRoute'
import { AppLayout } from '@/layouts/AppLayout'
import { HomePage } from '@/pages/HomePage'

export const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" />} />
      
      <Route path="/home" element={<AppLayout />}>
        <Route index element={<HomePage />} />
      </Route>

      <Route element={<AuthRoute />}>
        <Route path="/admin/*" element={null} />
      </Route>

      <Route path="*" element={<Navigate to="/home" replace />} />
    </Routes>
  )
}
