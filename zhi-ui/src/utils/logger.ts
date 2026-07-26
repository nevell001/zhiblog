type LogMethod = (...args: unknown[]) => void

export function createLogger(isProduction = Boolean(import.meta.env?.PROD)) {
  const shouldLog = () => !isProduction

  function runLog(method: LogMethod, args: unknown[]) {
    if (shouldLog()) {
      method(...args)
    }
  }

  return {
    warn: (...args: unknown[]) => runLog(console.warn, args),
    error: (...args: unknown[]) => runLog(console.error, args)
  }
}

export const logger = createLogger()

export default logger
