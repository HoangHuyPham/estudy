import { Button, Dropdown, MenuProps, Space } from 'antd'
import { DownOutlined, ShoppingCartOutlined, UserOutlined } from '@ant-design/icons'
import { Link, useNavigate } from 'react-router'
import { useUserContext } from '@hooks'
import { useEffect } from 'react'

import { toast } from 'react-toastify'
import { USER_ACTION } from '@contexts/UserContext'
// import JWTUtil from '../helper/JWTUtil'
import { IUserInfo } from '@interfaces';
import { Endpoint, AppRequest } from '@requests';

export const Header: React.FC = () => {
  const { user, dispatchUser } = useUserContext()
  const navigate = useNavigate()

  useEffect(() => {
       fetchUser()
  }, [])

  const fetchUser = async () => {
    const { status, data } = await AppRequest.getInstance().get(Endpoint.PROFILE_URL)
    
    if (status === 200){
        dispatchUser({
            type: USER_ACTION.ADD,
            payload: data?.data as IUserInfo
        })
    }else{
        toast.error(`Error: ${data}`)
    } 
  }

  const handleLogout = () => {
    dispatchUser({
      type: USER_ACTION.DELETE,
      payload: null
    })
    navigate("/auth/login")
  }

  const adminControl = [
    {
      key: '99',
      danger: true,
      label: <Link to={'/admin'}>Dashboard</Link>
    },
    {
      key: '1',
      label: <Link to={'/me'}>Profile</Link>
    },
    {
      key: '2',
      label: <Link to={'/order_history'}>Order History</Link>
    },
    {
      key: '3',
      danger: true,
      label: <a onClick={handleLogout}>Logout</a>,
    },
  ]

  const customerControl = [
    {
      key: '1',
      label: <Link to={'/me'}>Profile</Link>
    },
    {
      key: '2',
      label: <Link to={'/order_history'}>Order History</Link>
    },
    {
      key: '3',
      danger: true,
      label: <a onClick={handleLogout}>Logout</a>,
    },
  ]

  const items: MenuProps['items'] = 
  // user?.role.name === "Admin"?
  // adminControl:
  customerControl

  return (
    <>
      <header className="Header bg-[#3b82f6] flex sticky top-0 z-50 w-[100%] justify-between py-2 px-5">
        <Link to={"/home"}><img className='h-[35px]' src={"/icon.png"} /></Link>
        <section className='flex gap-2'>
          {
            user && (<>
              <Link to={"/home/cart"}><Button type="primary"><ShoppingCartOutlined /></Button></Link>
              <Button type="primary" danger>
                <Dropdown menu={{ items }}>
                  <a onClick={(e) => e.preventDefault()}>
                    <Space>
                      {`${user?.username} (${user?.role?.name})`}
                      <DownOutlined />
                    </Space>
                  </a>
                </Dropdown>
              </Button>

            </>) || (<>
              <Link to={"/home/cart"}><Button type="primary"><ShoppingCartOutlined /></Button></Link>
              <Link to={"/login"}><Button type="primary" danger icon={<UserOutlined />}>Login</Button></Link>
            </>)
          }
        </section>
      </header>
    </>
  )
}