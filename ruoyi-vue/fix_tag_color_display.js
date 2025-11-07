// 修复标签颜色显示问题的JavaScript代码

// 1. 确保颜色值格式正确
function normalizeColor(color) {
  if (!color) return '#409EFF'; // 默认颜色
  
  // 确保颜色值以#开头
  if (color.startsWith('#')) {
    return color;
  }
  
  // 如果颜色值不以#开头，添加#
  return '#' + color;
}

// 2. 在前端页面中添加颜色验证
function validateColor(color) {
  const colorRegex = /^(#([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})|rgb\(\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*\d{1,3}\s*\)|rgba\(\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*(0|1|0\.\d+)\s*\)|hsl\(\s*\d{1,3}\s*,\s*\d{1,3}%\s*,\s*\d{1,3}%\s*\)|hsla\(\s*\d{1,3}\s*,\s*\d{1,3}%\s*,\s*\d{1,3}%\s*,\s*(0|1|0\.\d+)\s*\))$/;
  return colorRegex.test(color);
}

// 3. 在Vue组件中使用
export function useTagColor() {
  return {
    normalizeColor,
    validateColor
  };
}