export interface IRole {
    id?: string,
    name?: string,
    description?: string
}

export interface IProfile {
    id?: string,
    firstName?: string,
    lastName?: string,
    email?: string,
    phone?: string
}

export interface ICourse {
    id?: string,
    name?: string,
    description?: string,
    language?: string,
    oldPrice?: number,
    currentPrice?: number,
    preview?: IImage,
    instructor?: IInstructor
    sections?: ISection[]
}

export interface ISection {
    id?: string,
    name?: string,
    ordinal?: number
    lectures?: ILecture[]
}

export interface ILecture {
    id?: string,
    title?: string,
    description?: number,
    ordinal?: number,
    video?: IVideo
}

export interface IVideo {
    id?: string,
    duration?: number,
    url?: string
}
export interface IInstructor {
    id?: string,
    description?: string,
    displayName?: string
}
export interface IImage {
    id?: string,
    url?: string,
    name?: string
}

export interface ICart {
    id?: string,
    cartItems?: ICartItem[]
}

export interface ICartItem {
    id?: ICartItemId,
    isSelected?: boolean,
    course?: ICourse
}

export interface ICartItemId {
    cartId?: string,
    courseId?: string,
}

export interface IRole {
    id?: string,
    name?: string,
    description?: string
}

export interface ICustomer{
    id?: string,
    displayName?: string,
    description?: string
}
export interface IInstructor{
    id?: string,
    displayName?: string,
    description?: string
}

export interface IUserInfo {
    id?: string,
    username?: string,
    role?: IRole,
    profile?: IProfile,
    customer?: ICustomer,
    instructor?: IInstructor
}

export interface IProfile {
    id?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    phone?: string;
    preview?: IImage;
}