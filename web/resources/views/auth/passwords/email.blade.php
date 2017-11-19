@extends('layouts.app')

@section('content')
<main class="bd-masthead" id="content" role="main">
  <div class="container">
    <div class="row">
      <div class="col-lg-8 mx-md-auto">
        <div class="card card-default">
          <div class="card-header">Reset Password</div>
          <div class="card-body">
            @if (session('status'))
                <div class="alert alert-success">
                    {{ session('status') }}
                </div>
            @endif
            <form class="form-horizontal" method="POST" action="{{ route('password.email') }}">
                {{ csrf_field() }}

              <div class="form-group{{ $errors->has('email') ? ' has-error' : '' }}">
                <label for="email" class="row form-control-label">E-Mail Address</label>
                <div class="row">
                  <input id="email" type="email" class="form-control" name="email" value="{{ old('email') }}" required>
                  @if ($errors->has('email'))
                      <span class="help-block">
                          <strong>{{ $errors->first('email') }}</strong>
                      </span>
                  @endif
                </div>
              </div>

              <div class="form-group">
                  <div class="col-md-6 col-md-offset-4  mx-md-auto">
                      <button type="submit" class="btn btn-primary">
                          Send Reset Link
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
