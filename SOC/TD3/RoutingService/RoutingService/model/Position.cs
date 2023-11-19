﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoutingService.model
{
    class Position
    {
        public double Lat { get; set; }
        public double Lng { get; set; }

        public override string ToString()
        {
            return $"Latitude: {Lat}, Longitude: {Lng}";
        }

        // Calculate distance to another Position object in meters
        public double DistanceTo(Position other)
        {
            var earthRadiusKm = 6371;

            var dLat = DegreesToRadians(other.Lat - this.Lat);
            var dLon = DegreesToRadians(other.Lng - this.Lng);

            var lat1 = DegreesToRadians(this.Lat);
            var lat2 = DegreesToRadians(other.Lat);

            var a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                    Math.Sin(dLon / 2) * Math.Sin(dLon / 2) * Math.Cos(lat1) * Math.Cos(lat2);
            var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
            return earthRadiusKm * c * 1000;
        }

        private static double DegreesToRadians(double degrees)
        {
            return degrees * Math.PI / 180.0;
        }
    }
}
