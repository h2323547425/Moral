# react-facebook-share-link

Create "share this on Facebook" links in React.

## Installation

With npm:

```bash
npm install --save react-facebook-share-link
```

Or with Yarn:

```bash
yarn add react-facebook-share-link
```

## Usage

```jsx
import ShareLink from 'react-facebook-share-link'

...

<ShareLink link='https://your-site.com/some-page'>
   {link => (
      <a href={link} target='_blank'>Share this on Facebook</a>
   )}
</ShareLink>
```

If you don't pass in a `link` prop, it will use the current page (`window.location.href`).