<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    /**
     * Authenticate user and return token.
     */
    public function login(Request $request)
    {
        $request->validate([
            'barcode' => 'required',
        ]);

        $user = User::where('barcode', $request->barcode)->first();

        if (! $user) {
            throw ValidationException::withMessages([
                'barcode' => ['Неверный код доступа.'],
            ]);
        }

        return response()->json([
            'status' => 'success',
            'token' => $user->createToken('auth_token')->plainTextToken,
            'user' => $user
        ]);
    }

    /**
     * Log out and revoke token.
     */
    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();
        return response()->json(['message' => 'Logged out']);
    }
}
