import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const BlogTokenKey = 'Blog-Token'

/**
 * 获取管理员 Token
 */
export function getToken(): string | undefined {
  return Cookies.get(TokenKey)
}

/**
 * 设置管理员 Token
 */
export function setToken(token: string): string | undefined {
  return Cookies.set(TokenKey, token)
}

/**
 * 移除管理员 Token
 */
export function removeToken(): void {
  Cookies.remove(TokenKey)
}

/**
 * 获取博客用户 Token
 */
export function getBlogToken(): string | undefined {
  return Cookies.get(BlogTokenKey)
}

/**
 * 设置博客用户 Token
 */
export function setBlogToken(token: string): string | undefined {
  return Cookies.set(BlogTokenKey, token)
}

/**
 * 移除博客用户 Token
 */
export function removeBlogToken(): void {
  Cookies.remove(BlogTokenKey)
}
