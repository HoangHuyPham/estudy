import { useEffect, useState } from 'react'
import { Carousel, Dropdown, Input, InputNumber, MenuProps, Pagination, Space, Typography } from 'antd';
import ProductList from '@components/ProductList';
import { DownOutlined } from '@ant-design/icons';
import { Endpoint, AppRequest } from '@requests';
import { IProductItem, IUser } from '../../interfaces';
import { useDebounce } from '../../custom/hooks';
import { useUserContext } from '../../contexts/hooks';
import { toast } from 'react-toastify';
import { USER_ACTION } from '../../contexts/UserContext';

const Home: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [totalItems, setTotalItems] = useState(0)

  const [products, setProducts] = useState<IProductItem[]>()
  const [categoryId, setCategoryId] = useState<string>("3381e3d3-1599-4c73-885f-e8cda0c4ca99")
  const [categoryMenus, setCategoryMenus] = useState<MenuProps['items']>([])
  const [loading, setLoading] = useState(false)

  const [minPrice, setMinPrice] = useState(0)
  const [maxPrice, setMaxPrice] = useState(1_000_000_000)

  const { dispatchUser } = useUserContext()

  const [keyword, setKeyword] = useState<string>();
  const keywordDebounced = useDebounce(keyword, 1000);

  useEffect(() => {
    try {
      setLoading(true)
      fetchProducts()
    } catch (error) {
      console.warn(error)
    } finally {
      setTimeout(() => {
        setLoading(false)
      }, 100);
    }
  }, [products?.length, pageSize, currentPage, categoryId, keywordDebounced, minPrice, maxPrice])

  useEffect(() => {
    fetchUser()
    fetchCategories()
  }, [])

  const fetchUser = async () => {
    try {
        const { status, data } = await get({
            url: Endpoint.PROFILE_URL
        })

        if (status === 200) {
            dispatchUser({
                type: USER_ACTION.ADD,
                payload: data as IUser
            })
        } else {
            toast.error(`Error: ${data}`)
        }
    } catch (error) {
        console.error(`Error: ${error}`)
    }
}

  const fetchCategories = async () => {
    // const { status, data } = await get({
    //   url: Endpoint.CATEGORY_URL
    // })
    // const categoryMenus: MenuProps['items'] = [{
    //   key: '3381e3d3-1599-4c73-885f-e8cda0c4ca99',
    //   label: 'No filter'
    // }]
    // if (status === 200)
    //   data.map(c => {
    //     categoryMenus?.push({
    //       key: c.id,
    //       label: c.name
    //     })
    //   })
    // setCategoryMenus(categoryMenus)
  }

  const fetchProducts = async () => {
    let params

    if (categoryId === "3381e3d3-1599-4c73-885f-e8cda0c4ca99")
      params = {
        MinPrice: minPrice,
        MaxPrice: maxPrice,
        Page: currentPage,
        PageSize: pageSize,
        KeyWord: keywordDebounced
      }
    else
      params = {
        CategoryId: categoryId,
        MinPrice: minPrice,
        MaxPrice: maxPrice,
        Page: currentPage,
        PageSize: pageSize,
        KeyWord: keywordDebounced
      }
    try {
      const { data, status } = await get({
        url: Endpoint.PRODUCT_URL,
        params: params
      })
      setTotalItems(data.totalItems)
      setProducts([...data.items])
    } catch (error) {
      setTotalItems(0)
      setProducts([])
    }
  }

  const handlePagination = (page: number, pageSize: number) => {
    setPageSize(pageSize)
    setCurrentPage(page)
  }

  const hanldeChangeCategory: MenuProps['onClick'] = ({ key }) => {
    setCategoryId(key)
  };

  return (
    <>
      <div className='Home flex flex-col items-center py-5 gap-5'>
        <div className="min-w-[1024px] max-w-[1024px]">
          <Carousel autoplay>
            <div className='flex justify-center'>
              <img lazy-loading={true} className='mx-auto' src='https://d1csarkz8obe9u.cloudfront.net/posterpreviews/smart-phone-banner-design-template-caa98978d25e965873a22b01acb99ba7_screen.jpg' />
            </div>
            <div className='flex justify-center'>
              <img lazy-loading={true} className='mx-auto' src='https://d1csarkz8obe9u.cloudfront.net/posterpreviews/smart-phone-banner-design-template-caa98978d25e965873a22b01acb99ba7_screen.jpg' />
            </div>
          </Carousel>
        </div>

        <div className="FilterWrapper flex gap-5 items-center">
          <div className="SearchWrapper w-[240px]">
            <Input onChange={(e) => setKeyword(e?.target?.value)} size="small" placeholder="search here" />
          </div>


          <Dropdown
            menu={{
              onClick: hanldeChangeCategory,
              items: categoryMenus,
              selectable: true,
              defaultSelectedKeys: ['3381e3d3-1599-4c73-885f-e8cda0c4ca99']
            }}
          >
            <Typography.Link>
              <Space>
                Category
                <DownOutlined />
              </Space>
            </Typography.Link>
          </Dropdown>

          <div className="PriceFilterWrapper flex text-black items-center">
            <InputNumber
              placeholder="min"
              onChange={v => setMinPrice(v)}
              value={minPrice}
              min={0}
              max={1_000_000_000}
              style={{ margin: '0 16px', width: '120px' }}
            />
            -
            <InputNumber
              placeholder="max"
              onChange={v => setMaxPrice(v)}
              value={maxPrice}
              min={0}
              max={1_000_000_000}
              style={{ margin: '0 16px', width: '120px' }}
            />
          </div>
        </div>

        {
          products != null && products?.length > 0 && (<ProductList loading={loading} products={products} />) || (
            <h1 className='text-black'>No available items :(</h1>
          )
        }

        <div className="flex justify-center">
          <Pagination simple defaultCurrent={1} total={totalItems} showSizeChanger onChange={handlePagination} />
        </div>
      </div>


    </>
  )
}

export default Home