<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Trip extends Model
{
  protected $fillable = [
    'reference',
    'from_latitude',
    'from_longitude',
    'to_latitude',
    'to_longitude',
    'status',
    'user_id'
  ];

  public function user() {
    return $this->belongsTo(\App\User::class);
  }

  public function events() {
    return $this->belongsTo(\App\Event::class);
  }
}
