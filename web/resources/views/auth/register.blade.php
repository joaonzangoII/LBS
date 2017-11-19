@extends('layouts.app')
@section('content')
<main class="bd-masthead" id="content" role="main">
  <div class="container">
    <div class="row">
      <div class="col-lg-8 mx-md-auto">
        <div class="card bg-default">
          <div class="card-header">Register</div>
          <div class="card-body">
            <form class="form-horizontal" method="POST" action="{{ route('register') }}">
                {{ csrf_field() }}
                <div class="form-group  has-feedback{{ $errors->has('name') ? ' has-error' : '' }}">
                  <label for="name" class="row form-control-label">Name</label>
                  <div class="row">
                    <input id="name"
                           type="text"
                           class="form-control"
                           name="name"
                           value="{{ old('name') }}"
                           autofocus>
                    @if ($errors->has('name'))
                      <span class="help-block">
                        <strong>{{ $errors->first('name') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group  has-feedback{{ $errors->has('email') ? ' has-error' : '' }}">
                  <label for="email" class="row form-control-label">E-Mail Address</label>
                  <div class="row">
                    <input id="email"
                           type="email"
                           class="form-control"
                           name="email"
                           value="{{ old('email') }}">

                    @if ($errors->has('email'))
                      <span class="help-block">
                        <strong>{{ $errors->first('email') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group  has-feedback{{ $errors->has('contact') ? ' has-error' : '' }}">
                  <label for="contact" class="row form-control-label">Contact</label>
                  <div class="row">
                    <input id="contact"
                           type="contact"
                           class="form-control"
                           name="contact"
                           value="{{ old('contact') }}">

                    @if ($errors->has('contact'))
                      <span class="help-block">
                        <strong>{{ $errors->first('contact') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group  has-feedback{{ $errors->has('id_number') ? ' has-error' : '' }}">
                  <label for="id_number" class="row form-control-label">ID Number</label>
                  <div class="row">
                    <input id="id_number"
                           type="id_number"
                           class="form-control"
                           name="id_number"
                           value="{{ old('id_number') }}">

                    @if ($errors->has('id_number'))
                      <span class="help-block">
                        <strong>{{ $errors->first('id_number') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group  has-feedback{{ $errors->has('address') ? ' has-error' : '' }}">
                  <label for="address" class="row form-control-label">Address</label>
                  <div class="row">
                    <input id="address"
                           type="address"
                           class="form-control"
                           name="address"
                           value="{{ old('address') }}">

                    @if ($errors->has('address'))
                      <span class="help-block">
                        <strong>{{ $errors->first('address') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>

                <div class="form-group  has-feedback{{ $errors->has('password') ? ' has-error' : '' }}">
                  <label for="password" class="row form-control-label">Password</label>
                  <div class="row">
                    <input id="password" type="password" class="form-control" name="password">
                    @if ($errors->has('password'))
                      <span class="help-block">
                        <strong>{{ $errors->first('password') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group  has-feedback">
                  <label for="password-confirm" class="row form-control-label">Confirm Password</label>
                  <div class="row">
                    <input id="password-confirm"
                           type="password"
                           class="form-control"
                           name="password_confirmation">
                  </div>
                </div>
                <div class="form-group  has-feedback">
                  <div class="row col-md-offset-4">
                    <button type="submit" class="btn btn-primary">
                        Register
                    </button>
                  </div>
                </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>
@endsection
