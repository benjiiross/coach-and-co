import type { ComponentProps } from "react";

interface H1Props extends ComponentProps<"h1"> {}

export const H1 = ({ children, className, ...rest }: H1Props) => {
	return (
		<h1
			className={`mb-12 text-4xl font-bold text-center ${className || ""}`}
			{...rest}
		>
			{children}
		</h1>
	);
};
