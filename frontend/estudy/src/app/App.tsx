import { BrowserRouter } from 'react-router-dom'
import { AppRoutes } from '@/routes/AppRoutes'
import { useEffect } from 'react'
import { useAppStore } from '@/store/AppStore'
import { isExpiredAT, parseAT } from '@/lib/utils'

export const App = () => {
  const setUserData = useAppStore(state => state.setUserData)

  useEffect(() => {
    const token = localStorage.getItem('accessToken')
    if (!token || isExpiredAT(token)) {
      console.warn("Guest access");
      return;
    }
    const userData = parseAT(token)
    console.log("Authenticated user:", userData);
    setUserData(userData)
  }, [])

  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  )
}
