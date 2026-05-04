import { Navigate, Outlet } from 'react-router-dom'

export const AuthRoute = () => {
  const isAuthenticated = Boolean(localStorage.getItem('access_token'))

  if (!isAuthenticated) {
    return <Navigate to="/auth" replace />
  }

  return <Outlet />
}
