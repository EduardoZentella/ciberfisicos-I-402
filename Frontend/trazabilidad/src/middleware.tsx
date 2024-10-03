import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';

export function middleware(request: NextRequest) {
  const authCookie = request.cookies.get('auth');
  const loginPath = new URL('/Login', request.url);
  const dashboardPath = new URL('/Dashboard', request.url);

  if (!authCookie && request.nextUrl.pathname !== loginPath.pathname) {
    return NextResponse.redirect(loginPath);
  }

  if (
    authCookie &&
    (request.nextUrl.pathname === '/' ||
      request.nextUrl.pathname === loginPath.pathname)
  ) {
    return NextResponse.redirect(dashboardPath);
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!_next|favicon.ico).*)'],
};
