import withYAML from 'next-yaml';
import path from 'path';
import { fileURLToPath } from 'url';

// Definir __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/** @type {import('next').NextConfig} */
const nextConfig = withYAML({
  webpack: (config) => {
    config.resolve.alias['@'] = path.resolve(__dirname, 'src');
    return config;
  },
});

export default nextConfig;
