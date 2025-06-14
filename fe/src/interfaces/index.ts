export interface IUser {
    id?: string,
    username?: string,
    role?: IRole,
    profile?: IProfile
}

export interface IRole{
    id?: string,
    name?: string,
    description?: string
}

export interface IProfile{
    id?: string,
    firstName?: string,
    lastName?: string,
    email?: string,
    phone?: string
}

export interface ICourse{
    id?: string,
    name?: string,
    description?: string,
    language?: string,
    duration?: string
}