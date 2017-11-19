<!doctype html>
<html lang="{{ app()->getLocale() }}">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="generator" content="Jekyll v3.6.0">
    <title>{{ env('APP_NAME')}} &middot;.</title>
    <!-- Bootstrap core CSS -->
    <link href="{{ asset('landing/dist/css/bootstrap.min.css') }}" rel="stylesheet">
    <!-- Documentation extras -->
    <link href="{{ asset('landing/css/docs.min.css') }}" rel="stylesheet">
    {{-- <link href="{{ asset('css/app.css') }}" rel="stylesheet"> --}}
    <!-- Favicons -->
    <link rel="apple-touch-icon" href="{{ asset('landing/img/favicons/apple-touch-icon.png') }}" sizes="180x180">
    <link rel="icon" href="{{ asset('landing/img/favicons/favicon-32x32.png') }}" sizes="32x32" type="image/png">
    <link rel="icon" href="{{ asset('landing/img/favicons/favicon-16x16.png') }}" sizes="16x16" type="image/png">
    <link rel="manifest" href="{{ asset('landing/img/favicons/manifest.json') }}">
    <link rel="mask-icon" href="{{ asset('landing/img/favicons/safari-pinned-tab.svg') }}" color="#563d7c">
    <link rel="icon" href="favicon.ico">
    <meta name="msapplication-config" content="{{ asset('landing/img/favicons/browserconfig.xml')}}">
    <meta name="theme-color" content="#563d7c">
    <!-- Meta -->
    <meta name="description" content="The most popular HTML, CSS, and JS library in the world.">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <!-- Twitter -->
    <meta name="twitter:site" content="@lbs">
    <meta name="twitter:creator" content="@lbs">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="Bootstrap">
    <meta name="twitter:description" content="The most popular HTML, CSS, and JS library in the world.">
    <meta name="twitter:image" content="{{ asset('landing/brand/bootstrap-social.png') }}">
    <!-- Facebook -->
    <meta property="og:url" content="{{route('home')}}">
    <meta property="og:title" content="{{env('APP_NAME')}}">
    <meta property="og:description" content="The most popular HTML, CSS, and JS library in the world.">
    <meta property="og:image" content="{{ asset('landing/brand/bootstrap-social.png') }}">
    <meta property="og:image:secure_url" content="{{ asset('https://getbootstrap.com/landing/brand/bootstrap-social.png') }}">
    <meta property="og:image:type" content="image/png">
    <meta property="og:image:width" content="1200">
    <meta property="og:image:height" content="630">
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
      ga('create', 'UA-146052-10', 'getbootstrap.com');
      ga('send', 'pageview');
    </script>

    <style text="text/css">
      h6.logo {
        vertical-align: middle;
        border-style: none;
      }

      .bd-navbar {
        position: static;
      }

      .bd-masthead {
        padding-top: 3rem;
        padding-bottom: 5rem;
      }
    </style>
  </head>
  <body>
    <a id="skippy" class="sr-only sr-only-focusable" href="index.html#content">
      <div class="container">
        <span class="skiplink-text">Skip to main content</span>
      </div>
    </a>
    <header class="navbar navbar-expand navbar-dark flex-column flex-md-row bd-navbar">
      <a class="navbar-brand mr-0 mr-md-2" href="{{ route('home')}}" aria-label="{{ env('APP_NAME')}}">
        <img height="40px" class="logo" src="{{ asset('landing/img/logolbs.png') }}" alt="">
      </a>
      <div class="navbar-nav-scroll">
        <ul class="navbar-nav bd-navbar-nav flex-row">
          <li class="nav-item">
            <a class="nav-link" href="{{ route('home') }}"
               onclick="ga('send', 'event', 'Navbar', 'Community links', 'Blog');"
               rel="noopener">{{ env('APP_NAME')}}
             </a>
          </li>
        </ul>
      </div>
      <ul class="navbar-nav flex-row ml-md-auto d-none d-md-flex">
        @if(Auth::guest())
        <li class="nav-item">
          <a class="nav-link " href="{{ route('login') }}"
              onclick="ga('send', 'event', 'Navbar', 'Community links', 'Docs');">Login</a>
        </li>
        <li class="nav-item">
          <a class="nav-link " href="{{ route('register') }}"
              onclick="ga('send', 'event', 'Navbar', 'Community links', 'Docs');">Register</a>
        </li>
        @else
          <li class="nav-item dropdown">
            <a class="nav-item nav-link dropdown-toggle mr-md-2"
               href="index.html#"
               id="bd-versions"
               data-toggle="dropdown"
               aria-haspopup="true"
               aria-expanded="false">
              Dashboard
            </a>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="bd-versions">
              <a class="dropdown-item active" href="docs/4.0/index.html">Latest (4.x)</a>
              <a class="dropdown-item" href="https://v4-alpha.getbootstrap.com/">v4 Alpha 6</a>
            </div>
          </li>
        @endif
      </ul>
    </header>
    <main role="main">
      <section>
        <div id="app">
          @yield('content')
        </div>
      </section>
    </main>
    <footer class="bd-footer text-muted">
      <div class="container-fluid p-3 p-md-5">
        <ul class="bd-footer-links">
          <li><a href="#">Facebook</a></li>
        </ul>
        <p>Designed and built with all the love in the world by
          <a href="https://twitter.com/mdo"target="_blank" rel="noopener">@mdo</a> and
          Maintained by the
          <a href="https://github.com/orgs/twbs/people">core team</a> with the help of
          <a href="https://github.com/twbs/bootstrap/graphs/contributors">our contributors</a>.</p>
      </div>
    </footer>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="{{asset('landing/js/vendor/jquery-slim.min.js')}}"><\/script>')</script>
    <script src="{{ asset('landing/js/vendor/popper.min.js') }}"></script>
    <script src="{{ asset('landing/dist/js/bootstrap.min.js') }}"></script>
    <script src="{{ asset('landing/js/docs.min.js') }}"></script>
    <script src="{{ asset('landing/js/ie-emulation-modes-warning.js') }}"></script>
    <!-- Scripts -->
    <script src="{{ asset('js/app.js') }}"></script>
    <script>
      Holder.addTheme('gray', {
        bg: '#777',
        fg: 'rgba(255,255,255,.75)',
        font: 'Helvetica',
        fontweight: 'normal'
      });
    </script>
  </body>
</html>
