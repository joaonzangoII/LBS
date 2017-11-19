<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;
use App\User;
use App\Trip;

class UsersController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
      return response()->json(User::latest()->get());
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }


    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
      $user = User::findOrFail($id);
      $rules = [
        'name' => 'required|string',
        'id_number' => 'required|string',
        'contact' => 'required|string',
        'email' => 'required|email|max:255',//,email,'. $user->email,
        'address' => 'required|string',
      ];

      $email = $request->input('email');
      $validator = Validator::make($request->all() , $rules);

      if($validator->fails()){
        return response()->json([
          'success' => false,
          'messages' => $validator->messages()
        ]);
      }

      $user =  $user->update([
        'name' => $request->input('name'),
        'contact' => $request->input('contact'),
        'id_number' => $request->input('id_number'),
        'email' => $request->input('email'),
        'address' => $request->input('address'),
      ]);

      $data = [
        'success' => true,
        'user' => User::findOrFail($id)
      ];
      // try{
      //   $user->notify(new UsuarioFezCadastro($user));
      // }catch(\Exception $e){
      //   return $data;
      // }
      return $data;
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function trips($id)
    {
      $trips = Trip::with('user','events')->where('user_id', $id)->latest()->get();
      $data = [
        'success' => true,
        'data' => $trips
      ];

      return response()->json($data);
    }
}
