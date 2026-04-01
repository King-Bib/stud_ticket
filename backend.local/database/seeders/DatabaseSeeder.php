<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // \App\Models\User::factory(10)->create();

        \App\Models\User::factory()->create([
            'name' => 'Библев Артём',
            'email' => 'artem@example.com',
            'password' => bcrypt('password123'),
            'fio' => 'Библев Артём Викторович',
            'group' => 'ИСП.23А',
            'organization' => 'ГОУ ВО МО ПЭК ГГТУ',
        ]);
    }
}
