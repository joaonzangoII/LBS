<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;
use App\Trip;
use App\Event;

class TripsController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
      $trips = Trip::with('user', 'events')->latest()->get();
      return response()->json($trips);
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
      $reqData = $request->all();
      $reqData['reference'] = $this->generateReferenceNumber();
      $rules = [
        'from_latitude'  => 'required',
        'from_longitude' => 'required',
        'to_latitude'    => 'required',
        'to_longitude'   => 'required',
        'user_id'        => 'required'
      ];

      $validator = Validator::make($reqData, $rules);
      if($validator->fails()){
        return response()->json([
          'success' => false,
          'messages' =>$validator->messages()
        ]);
      }else{
        $trip = Trip::create($reqData);
        $data = [
         'success' => true,
         'data' => $trip
        ];

        return $data;
      }
    }

    function generateReferenceNumber() {
       $number = mt_rand(1000000000, 9999999999); // better than rand()

       // call the same function if the barcode exists already
       if ($this->barcodeNumberExists($number)) {
           return $this->generateBarcodeNumber();
       }

       // otherwise, it's valid and can be used
      return $number;
    }

    function barcodeNumberExists($number) {
      // query the database and return a boolean
      // for instance, it might look like this in Laravel
      return Trip::whereReference($number)->exists();
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
      $trip = Trip::with('user', 'events')->findOrFail($id);
      return response()->json($trip);
    }

    /**
     * Display the specified resource.
     *
     * @return \Illuminate\Http\Rsesponse
     */
    public function check(Request $request)
    {
      $rules = [
        'reference' => 'required',
        'user_id' => 'required',
      ];

      $validator = Validator::make($request->all(), $rules);

      if($validator->fails()){
        return response()->json([
          'success' => false,
          'messages' => $validator->messages()
        ]);
      }

      $trip = Trip::with('user')->where('reference', $request->input('reference'))->first();

      $trackable_status = ['requested', 'en-route'];
      $status = ['requested', 'en-route', 'arrived', 'cancelled'];
      if( $trip === null){
        $data =[
          'success' => false,
          'messages' => ['This trip does not exist'],
        ];
      } else if($trip->user_id === (int)$request->input('user_id')) {
        $data =[
          'success' => false,
          'messages' => ['You cannot track your own ride'],
        ];
      } else if(!in_array($trip->status, $trackable_status)) {
        $data =[
          'success' => false,
          'messages' => ['You cannot track arrived or cancelled trips'],
        ];
      } else {
        $data =[
          'success' => true,
          'data' => $trip,
        ];
      }

      return $data;
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function events($id)
    {
      $events = Event::with('trip')->where('trip_id', $id)->latest()->get();
      $data = [
        'success' => true,
        'data' => $events
      ];

      return response()->json($data);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function postEvents(Request $request, $id)
    {
      $rules = [
        'current_latitude' => 'required',
        'current_longitude' => 'required',
        'trip_id' => 'required',
      ];

      $validator = Validator::make($request->all(), $rules);

      if($validator->fails()){
        return response()->json([
          'success' => false,
          'messages' => $validator->messages()
        ]);
      }

      $event = Event::create($request->all());
      $data = [
       'success' => true,
       'data' => $event
      ];

      return $data;
    }

}
