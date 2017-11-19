<?php

use Faker\Generator as Faker;

/*
|--------------------------------------------------------------------------
| Model Factories
|--------------------------------------------------------------------------
|
| This directory should contain each of the model factory definitions for
| your application. Factories provide a convenient way to generate new
| model instances for testing / seeding your application's database.
|
*/

$factory->define(App\User::class, function (Faker $faker) {
    static $password;
    // $faker->addProvider(new Faker\Provider\en_US\PhoneNumber($faker));

    return [
        'name' => $faker->name,
        'contact' => $faker->phoneNumber,
        'id_number' => rand(60000, 999999),
        'email' => $faker->unique()->safeEmail,
        'address' => $faker->address,
        // 'image' => $faker->address,
        'password' => $password ?: $password = bcrypt('secret'),
        'remember_token' => str_random(10),
    ];
});
