<?php
namespace App\Services;

use App\User;
use Carbon\Carbon;
use Illuminate\Validation\Validator;

class CustomValidator extends Validator
{

  public function validateCorrect($attribute, $value, $parameters)
  {
    $match = preg_match("!^(\d{2})(\d{2})(\d{2})\d{7}$!", $value, $matches);
    if (!$match) {
      return false;
    }

    list ($id_number, $year, $month, $day) = $matches;
    $baseYear = 1950;
    $shortYear = $year;
    $year = 100 + $baseYear + ($shortYear - $baseYear) % 100;
    $dob = Carbon::create($year, $month, $day);

    if ($dob->diffInYears(Carbon::create()) < 16) {
      return false;
    }

    /**
    * Check that the date is valid
    */
    if (!checkdate($month, $day, $year)) {
      return false;
    }

    /**
     * Now Check the control digit
     */
    $d = -1;
    $a = 0;
    for ($i = 0; $i < 6; $i++) {
      $a += $value{2 * $i};
    }

    $b = 0;
    for ($i = 0; $i < 6; $i++) {
      $b = $b * 10 + $value{2 * $i + 1};
    }
    $b *= 2;

    $c = 0;
    do {
      $c += $b % 10;
      $b = $b / 10;
    } while ($b > 0);

    $c += $a;
    $d = 10 - ($c % 10);
    if ($d == 10) $d = 0;

    //    if ($value{strlen($value) - 1} == $d) {
    //      return true;
    //    }

    return true;
  }

  public function validateStrength($attribute, $value, $parameters, $validator)
  {
    if (preg_match('/(?=^.{8,}$)(?=.*\d)(?=.*[!@#$%^&*]+)(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/', $value)) {
      return true;
    }

    return false;
  }

  public function validateDateOfBirth($attribute, $value, $parameters, $validator)
  {
    $dob = Carbon::createFromFormat("Y-m-d", $value);
    $now = Carbon::now()->format("Y-m-d");
    $diff = $dob->diffInYears(Carbon::create());
    if($diff==0 || $diff < 18){
      return false;
    }

    if($dob == $now){
      return false;
    }

    return true;
  }


  public function validateContactNumber($attribute, $value, $parameters, $validator)
  {
    if(preg_match("/^27[0-9]{9}$/", $value)) {
      return true;
    }

    return false;
  }
}
