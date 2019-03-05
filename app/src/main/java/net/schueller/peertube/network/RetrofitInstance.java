/*
 * Copyright 2018 Stefan Schüller <sschueller@techdroid.com>
 *
 * License: GPL-3.0+
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.schueller.peertube.network;

import android.content.Context;
import android.content.Intent;
import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static String baseUrl;

    public static Retrofit getRetrofitInstance(String newBaseUrl, Context context) {
        if (retrofit == null || !newBaseUrl.equals(baseUrl)) {
            baseUrl = newBaseUrl;

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new AuthorizationInterceptor());

            // TODO: add Tor support
            Intent statusIntent = new Intent();
            statusIntent.putExtra(OrbotHelper.EXTRA_STATUS, OrbotHelper.STATUS_OFF);

            try {

                OkHttpClient client = StrongOkHttpClientBuilder
                        .forMaxSecurity(context)
                        .withWeakCiphers()
                        .applyTo(okHttpClientBuilder, statusIntent)
                        .build();

                retrofit = new retrofit2.Retrofit.Builder()
                        .client(client)
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return retrofit;
    }
}