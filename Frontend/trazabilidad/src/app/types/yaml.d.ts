declare module '*.yaml' {
  const content: { [key: string]: string };
  export default content;
}
