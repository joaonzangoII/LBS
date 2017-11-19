<?php

use Illuminate\Database\Seeder;
use App\User;
use App\Trip;
class TripsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
      Trip::truncate();
      $faker = Faker\Factory::create();

      foreach (User::all() as $key => $user) {
        for ($i=0; $i < rand(2,10); $i++) {
          $data = [
            "reference"     => $this->generateReferenceNumber(),
            'from_latitude'  => $faker->latitude(-27.001, -25.003),
            'from_longitude' => $faker->longitude(27.032,  28.033),
            'to_latitude'    => $faker->latitude(-27.001, -25.003),
            'to_longitude'   => $faker->longitude(27.032,  28.033),
            'user_id'        => $user->id,
          ];

          Trip::create($data);
        }
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

    // public function booking_number(){
    //  if(count(\DB::select("SELECT id FROM trips_sequences WHERE date = CURDATE()")) == 0){
    //    $sequence = 0;
    //  }else{
    //    $sequence = count(\DB::table('trips_sequences')->where('date', date("Y-m-d"))->pluck('id'));
    //  }
    //  $sequence = $sequence + 1;
    //  $code = "LBS";
    //  $b_number = $code . date("ymd") . str_pad($sequence, 4, '0', STR_PAD_LEFT);
    //  \DB::statement("INSERT INTO trips_sequences SET id = $sequence, date = CURDATE() ON DUPLICATE KEY UPDATE id = $sequence");
    //  return $b_number;
    // }
}
