<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        User::create([
            'name' => 'Artem B.',
            'email' => 'artem@example.com',
            'password' => Hash::make('password123'),
            'fio' => 'Библев Артём Викторович',
            'group' => 'ИСП.23А',
            'organization' => 'ГОУ ВО МО ПЭК ГГТУ',
        ]);
    }
}
