<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
Route::name('api.login')->post('/login', 'Api\AuthController@login');
Route::name('api.register')->post('/register', 'Api\AuthController@register');
Route::name('api.trips.check')->post('/trips/check', 'Api\TripsController@check');
Route::name('api.trips.events')->get('/trips/{trip}/events', 'Api\TripsController@events');
Route::name('api.trips.events.post')->post('/trips/{trip}/events', 'Api\TripsController@postEvents');
Route::resource(
 'trips',
 'Api\TripsController', [
   'names' => [
     'index' => 'api.trips',
     'show'=> 'api.trip.show',
     'store'=> 'api.trips.store',
   ],
   'only'  => [
     'index',
     'show',
     'store',
   ],
 ]);
Route::name('api.user.trips')->get('/user/trips/{user}', 'Api\UsersController@trips');
Route::resource(
 'users',
 'Api\UsersController', [
   'names' => [
     'index' => 'api.users',
     'show'=> 'api.user.show',
     'update'=> 'api.user.update',
   ],
   'only'  => [
     'index',
     'show',
     'update',
   ],
 ]);
