<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Event extends Model
{
  protected $fillable = [
    'current_latitude',
    'current_longitude',
    'trip_id'
  ];

  public function trip() {
    return $this->belongsTo(\App\Trip::class);
  }
}
