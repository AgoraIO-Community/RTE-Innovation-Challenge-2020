module.exports = {
    env: {
      browser: true,
      es2020: true,
    },
    globals: {
      uni: 'writable',
    },
    extends: [
      'plugin:vue/essential',
      'airbnb-base',
    ],
    parserOptions: {
      ecmaVersion: 11,
      sourceType: 'module',
    },
    plugins: [
      'vue',
      "babel"
    ],
    rules: {
      'linebreak-style': [0],
      'no-param-reassign': ['error', { props: false }],
      'func-names': ['warn', 'as-needed'],
      'no-plusplus': ['error', { allowForLoopAfterthoughts: true }],
      'no-underscore-dangle': ['error', { allow: ['foo_', '_this'] }],
      'import/no-unresolved': ['error', { ignore: ['@'] }],
    },
  };
    