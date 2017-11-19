@extends('layouts.app')
@section('content')
  <main class="bd-masthead" id="content" role="main">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-6 mx-auto col-md-5 order-md-2">
          <img class="img-fluid mb-3 mb-md-0" src="{{ asset('landing/img/location.png') }}"
               alt=""
               width="1024"
               height="860">
        </div>
        <div class="col-md-7 order-md-1 text-center text-md-left pr-md-5">
          <h1 class="mb-3 bd-text-purple-bright">{{ env('APP_NAME')}}</h1>
          <p class="lead">

          </p>
          <p class="lead mb-4">
            LBS application enables you to share your location while travelling
            using LBS traveling services by generating a unique code that you can
            send to your family or friends to allow them to track you throughout your journey.
          </p>
          <div class="d-flex flex-column flex-md-row lead mb-3">
            <a href="#"
               class="btn btn-lg btn-bd-purple mb-3 mb-md-0 mr-md-3"
               onclick="ga('send', 'event', 'Jumbotron actions', 'Get started', 'Get started');">
               Download
             </a>
            <a href="#"
               class="btn btn-lg btn-outline-secondary"
               onclick="ga('send', 'event', 'Jumbotron actions', 'Download', 'Download 4.0.0-beta.2');">
               Facebook
             </a>
          </div>
        </div>
      </div>
    </div>
  </main>
@endsection
