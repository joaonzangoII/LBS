<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'contact', 'id_number', 'email', 'password', 'address', 'image'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];


    public function trips() {
      return $this->hasMany(\App\Trip::class);
    }

    public function sent_messages() {
      return $this->hasMany(\App\ChatMessage::class, 'sender_id');
    }

    public function received_messages() {
      return $this->hasMany(\App\ChatMessage::class, 'receiver_id');
    }

    public function isAdmin(){

    }
}
