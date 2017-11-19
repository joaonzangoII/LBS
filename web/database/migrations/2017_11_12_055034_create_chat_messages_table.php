<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateChatMessagesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('chat_messages', function (Blueprint $table) {
            $table->increments('id');
            $table->text('subject')->nullable();
            $table->text('message')->nullable();
            $table->integer('sender_id')->unsigned()->index();
            $table->foreign('sender_id')
                  ->references('id')
                  ->on('users')
                  ->onDelete('cascade');
            $table->integer('receiver_id')->unsigned()->index();
            $table->foreign('receiver_id')
                  ->references('id')
                  ->on('users')
                  ->onDelete('cascade');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('chat_messages');
    }
}
