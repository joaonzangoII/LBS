@extends('layouts.app')

@section('content')
<main class="bd-masthead" id="content" role="main">
  <div class="container">
      <div class="row">
          <div class="col-lg-8 mx-md-auto">
              <div class="card card-default">
                  <div class="card-header">Reset Password</div>
                  <div class="card-body">
                      <form class="" method="POST" action="{{ route('password.request') }}">
                          {{ csrf_field() }}
                          <input type="hidden" name="token" value="{{ $token }}">
                          <div class="form-group{{ $errors->has('email') ? ' has-error' : '' }}">
                            <label for="email" class="row form-control-label">E-Mail Address</label>
                            <div class="row">
                              <input id="email" type="email" class="form-control" name="email" value="{{ $email or old('email') }}" required autofocus>
                              @if ($errors->has('email'))
                                <span class="help-block">
                                    <strong>{{ $errors->first('email') }}</strong>
                                </span>
                              @endif
                            </div>
                          </div>
                          <div class="form-group{{ $errors->has('password') ? ' has-error' : '' }}">
                            <label for="password" class="row form-control-label">Password</label>
                            <div class="row">
                                <input id="password" type="password" class="form-control" name="password" required>

                                @if ($errors->has('password'))
                                    <span class="help-block">
                                        <strong>{{ $errors->first('password') }}</strong>
                                    </span>
                                @endif
                            </div>
                          </div>
                          <div class="form-group{{ $errors->has('password_confirmation') ? ' has-error' : '' }}">
                              <label for="password-confirm" class="row form-control-label">Confirm Password</label>
                              <div class="row">
                                  <input id="password-confirm" type="password" class="form-control" name="password_confirmation" required>

                                  @if ($errors->has('password_confirmation'))
                                      <span class="help-block">
                                          <strong>{{ $errors->first('password_confirmation') }}</strong>
                                      </span>
                                  @endif
                              </div>
                          </div>

                          <div class="form-group">
                              <div class="row col-md-offset-4">
                                  <button type="submit" class="btn btn-primary">
                                      Reset Password
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
