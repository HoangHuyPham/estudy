import { useState } from "react"
import { ICartItem } from "../interfaces"
import logo from "/icon.png"
import { Button, Checkbox, InputNumber } from "antd"
import { useNavigate } from "react-router"


type CartItemProps = {
    cartItem: ICartItem
    removeCartItem: (cartItem: ICartItem) => void
    updateCartItem: (cartItem: ICartItem) => void
}

export const CartItem: React.FC<CartItemProps> = ({cartItem, removeCartItem, updateCartItem}) => {
    const navigate = useNavigate()
    const [totalPrice, setTotalPrice] = useState(cartItem.product.productDetail.price * cartItem.quantity)

    const handleQuantityChange = (value:number) => {
        if (value <= 0)
            removeCartItem(cartItem)
        else{
            cartItem.quantity = value
            updateCartItem(cartItem)
            setTotalPrice(cartItem.product.productDetail.price * value)
        }
    };

    return (
        <>
        <div className="flex w-[100%] px-5 py-2 mb-2 text-1xl-black text-black bg-[#ffffff] shadow-xl animate-fade">
          <span className="w-[40%] flex gap-2 items-center">
            <Checkbox onChange={()=>{
                cartItem.isSelected = !cartItem.isSelected
                updateCartItem(cartItem)
            }} checked={cartItem.isSelected}/>
            <img onClick={()=>{navigate(`/product/${cartItem.product.id}`)}} className="w-[120px] h-[120px] cursor-pointer" src={cartItem.product?.thumbnail?.url || logo} />
            <span>
                {cartItem.product.name} <br/>
                (<span className="text-sm text-red-500">{cartItem.productOption?.name || "No option"}</span>)
            </span>
            
          </span>
          <span className="w-[20%] flex justify-center items-center relative"><p className="absolute text-red-500 text-sm line-through bottom-8 left-0">{cartItem.product.productDetail.beforePrice > cartItem.product.productDetail.price && Number.formatPrice(cartItem.product.productDetail.beforePrice)}</p><p className="font-[500]">{Number.formatPrice(cartItem.product.productDetail.price)}</p></span>
          <span className="w-[10%] flex justify-center items-center">
          <InputNumber min={0} max={cartItem.product.productDetail.stock} value={cartItem.quantity} onChange={handleQuantityChange} />
          </span>
          <span className="w-[20%] flex font-bold justify-center items-center">{Number.formatPrice(totalPrice)}</span>
          <span className="w-[10%] flex justify-center items-center">
            <Button type="primary" danger onClick={()=>removeCartItem(cartItem)}>Remove</Button>
          </span>
        </div>    
        </>
    )
}