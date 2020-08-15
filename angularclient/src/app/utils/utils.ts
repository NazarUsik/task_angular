export function setCookie(name: string, value: string) {
  const d = new Date();
  d.setTime(d.getTime() + (7 * 24 * 60 * 60 * 1000));
  const expires = 'expires='.concat(d.toUTCString());
  document.cookie
    = name
    .concat('=').concat(value)
    .concat(';').concat(expires)
    .concat(';path=/');
}

export function getCookie(cname: string) {
  const name = cname + '=';
  const decodedCookie = decodeURIComponent(document.cookie);
  const ca = decodedCookie.split(';');
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) === ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length);
    }
  }
  return '';
}

export function deleteAllCookies() {
  const cookies = document.cookie.split(';');

  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i];
    const eqPos = cookie.indexOf('=');
    const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT';
  }
}

export function onLogout() {
  deleteAllCookies();
  setTimeout(window.location.reload(), 2000);
}
