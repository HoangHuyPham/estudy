import { BrowserRouter } from 'react-router'
import { AppRoutes } from '@routes'
import { UserProvider } from '@contexts/UserContext'

function App() {
  return (
    <>
      <BrowserRouter>
        <UserProvider>
          <AppRoutes />
        </UserProvider>
      </BrowserRouter>
    </>
  )
}

export default App
