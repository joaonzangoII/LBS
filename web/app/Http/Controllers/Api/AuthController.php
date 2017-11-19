<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;
use App\User;
use Auth;

class AuthController extends Controller
{
  public function login(Request $request)
  {
    $email = $request->input('email');
    $password = $request->input('password');

    $credentials = $request->only('email', 'password');
    $validator = Validator::make($credentials , [
        'email'=> 'required|string',
        'password' => 'required|string',
    ]);

    if($validator->fails()){
      return response()->json([
        'success' => false,
        'messages' => $validator->messages()
      ]);
    }

    if(!Auth::guard()->attempt($credentials, $request->has('remember'))){
      return response()->json([
        'success' => false,
        'messages' => [
          'email' => ['Invalid credentials']
        ]
      ]);
    }

    $user = User::where('email', '=',  $email)
                // ->with('user_type')
                ->first();

    // if($user->isAdmin())
    // {
    //   return response()->json([
    //     'code' => '500',
    //     'erro' => true,
    //     'messages' => [
    //       'email' => ['An A']
    //     ]
    //   ]);
    // }

    return response()->json([
      'success' => true,
      'user' => $user
    ]);
  }

  public function register(Request $request)
  {
    $rules = [
      'name' => 'required|string',
      'address' => 'required|string',
      'contact' => 'required|string',
      'id_number' => 'required|string|size:13|unique:users|correct',
      'email' => 'required|email|max:255|unique:users',
      'password' => 'required|min:6|confirmed',
      'image' => ''
    ];

    $email = $request->input('email');
    $validator = Validator::make($request->all() , $rules);

    if($validator->fails()){
      return response()->json([
        'success' => false,
        'messages' => $validator->messages()
      ]);
    }

    // $cliente = TipoDeUsuario::where('nome', 'cliente')->first();
    // $genero = Genero::where('nome', strtolower($request->input('sexo')))->first();
    $user =  User::create([
      'name' => $request->input('name'),
      'contact' => $request->input('contact'),
      'id_number' => $request->input('id_number'),
      'email' => $request->input('email'),
      'password' => bcrypt($request->input('password')),
      'address' => $request->input('address'),
      // 'user_type_id' => $cliente->id
      // 'image' => uploadBase64Image($request->input('image')),
    ]);

    $data = [
      'success' => true,
      'user' => $user
    ];
    // try{
    //   $user->notify(new UsuarioFezCadastro($user));
    // }catch(\Exception $e){
    //   return $data;
    // }
    return $data;
  }
}
